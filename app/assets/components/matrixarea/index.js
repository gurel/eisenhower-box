import angular from 'angular';
import { buildDirective } from '../../common/Directive';
import style from './matrixarea.css';

let template = `
  <div ng-class="titlestyle" class="${style.header}">
    <div class="pull-right btn btn-default btn-xs" ng-click="toogleShowAll()">{{showall ? 'Hide' : 'Show'}} Completed</div>
    <span>{{title}}</span>
    <span class="small"><br/>{{description}}</span>
  </div>
  <ul>
    <li ng-repeat="n in tasks | orderBy:'-urgency'" ng-class="{${style.done}: n.status == 'done'}" class="list-unstyled" ng-show="n.status === 'inprogress' || showall">
      <input type="checkbox" ng-model="n.status" ng-true-value="'done'" ng-false-value="'inprogress'" ng-change="taskChecked(n)"/>
      <span ng-click="taskClicked(n)">
        {{n.message.split('\n')[0]}}
      </span>
      <span ng-click="taskClicked(n)" class="pull-right label label-warning">U: {{n.urgency}}</span>
      <span ng-click="taskClicked(n)" class="pull-right label label-info">I: {{n.importance}}</span>
    </li>
  </ul>
  
`;

class MatrixAreaController {
  static $inject = ['$scope', '$element'];

  constructor($scope, $element) {
    this.$scope = $scope;
    this.$element = $element;

    angular.element($element).addClass(style.matrixarea);

    this.link();
  }
  link() {
    this.$scope.showall = false;
    this.$scope.taskClicked = this.taskClicked.bind(this);
    this.$scope.taskChecked = this.taskChecked.bind(this);
    this.$scope.toogleShowAll = this.toogleShowAll.bind(this);
  }
  taskClicked(task) {
    const expressionHandler = this.$scope.ontaskclicked;
    if(expressionHandler) {
      expressionHandler({task: task});
    }
  }
  taskChecked(task) {
    const expressionHandler = this.$scope.ontaskupdated;
    if(expressionHandler) {
      expressionHandler({task: task});
    }
  }
  toogleShowAll() {
    this.$scope.showall = !this.$scope.showall;
  }

}

export default buildDirective(
  'matrixarea', MatrixAreaController, {
    restrict: 'E',
    transclude: true,
    scope: {
      tasks: '=', title: '=', description: '=', titlestyle: '=',
      ontaskclicked: '&', ontaskupdated: '&'
    },
    template
  });
