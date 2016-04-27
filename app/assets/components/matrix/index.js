import angular from 'angular';
import MatrixArea from '../matrixarea';
import { buildDirective } from '../../common/Directive';
import style from './matrix.css';

let template = `
  <div class="panel panel-default">
    <div class="panel-body">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-2 hidden-sm hidden-xs ${style.matrixarea}">&nbsp;</div>
          <div class="col-md-5 hidden-sm hidden-xs text-center ${style.matrixarea}">Urgent</div>
          <div class="col-md-5 hidden-sm hidden-xs text-center ${style.matrixarea}">Not Urgent</div>
        </div>
        <div class="row">
          <div class="col-md-2 hidden-sm hidden-xs">
            <div> Important</div>
          </div>
          <matrixarea class="col-md-5 col-sm-6 col-xs-12 ${style.matrixarea}" titlestyle="'${style.important_urgent}'" tasks="important_urgent" ontaskupdated="taskUpdated(task)" ontaskclicked="taskClicked(task)" title="'Do'" description="'Do it now.'"></matrixarea>
          <matrixarea class="col-md-5 col-sm-6 col-xs-12 ${style.matrixarea}" titlestyle="'${style.important_noturgent}'" tasks="important_noturgent" ontaskupdated="taskUpdated(task)"  ontaskclicked="taskClicked(task)" title="'Decide'" description="'Schedule a time to do it.'"></matrixarea>
        </div>
        <div class="row row-eq-height">
          <div class="col-md-2 hidden-sm hidden-xs">
            <div>Not Important</div>    
          </div>
          <matrixarea class="col-md-5 col-sm-6 col-xs-12 ${style.matrixarea}" titlestyle="'${style.notimportant_urgent}'" tasks="notimportant_urgent" ontaskupdated="taskUpdated(task)"  ontaskclicked="taskClicked(task)" title="'Delegate'" description="'Who can do it for you?'"></matrixarea>
          <matrixarea class="col-md-5 col-sm-6 col-xs-12 ${style.matrixarea}" titlestyle="'${style.notimportant_noturgent}'" tasks="notimportant_noturgent" ontaskupdated="taskUpdated(task)"  ontaskclicked="taskClicked(task)" title="'Delete'" description="'Eliminate it.'"></matrixarea>
        </div>
      </div>
    </div>
  </div>
  `;

class MatrixController {
  static $inject = ['$scope', '$element', '$parse'];

  constructor($scope, $element) {
    this.$scope = $scope;
    this.$element = $element;
    angular.element($element).addClass(style.matrix);

    this.$scope.$watch('tasks', (newValue) => {
      this.$scope.important_urgent = newValue.filter(this._taskfilter.bind(this, 5, 10, 5, 10));
      this.$scope.notimportant_urgent = newValue.filter(this._taskfilter.bind(this, 0, 4, 5, 10));
      this.$scope.important_noturgent = newValue.filter(this._taskfilter.bind(this, 5, 10, 0, 4));
      this.$scope.notimportant_noturgent = newValue.filter(this._taskfilter.bind(this, 0, 4, 0, 4));
    });

    this.link();
  }
  link() {
    this.$scope.taskClicked = this.taskClicked.bind(this);
    this.$scope.taskUpdated = this.taskUpdated.bind(this);
  }
  _taskfilter(minImportance, maxImportance, minUrgency, maxUrgency, item) {
    return item.importance >= minImportance &&
      item.importance <= maxImportance &&
      item.urgency >= minUrgency &&
      item.urgency <= maxUrgency
  }
  taskClicked(task){
    const expressionHandler = this.$scope.ontaskclicked;
    if(expressionHandler) {
      expressionHandler({task: task});
    }
  }
  taskUpdated(task) {
    const expressionHandler = this.$scope.ontaskupdated;
    if(expressionHandler) {
      expressionHandler({task: task});
    }
  }
}

export default buildDirective(
  'matrix', MatrixController, {
    restrict: 'E',
    transclude: true,
    scope: {
      tasks: '=',
      ontaskclicked: '&',
      ontaskupdated: '&'
    },
    template
  }, [MatrixArea]);
