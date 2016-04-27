import angular from 'angular';
import angularRoute from 'angular-route';
import { allComponents } from './components';
import { allServices, TaskService } from './services';
import { allProviders, ModalProvider } from './providers';
import Task from './services/task/Task';

import 'ng-dialog/css/ngDialog.min.css';
import 'ng-dialog/css/ngDialog-theme-default.min.css';
import './app.global.css';
import style from './app.css';


let template = `
  <div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
          <div class="btn btn-primary pull-right" ng-click="app.openTask()"><i class="glyphicon glyphicon-plus"></i> Add New Task</div>      
        </div> 
    </div>
  </div>
  <matrix tasks="app.tasks" ontaskclicked="app.openTask(task)" ontaskupdated="app.updateTask(task)"></matrix>
`;
let editorTemplate = `
<div class="container-fluid ${style.editorDialog}">
  <div class="row">
    <div class="col-md-2">Task</div>
    <div class="col-md-10"><textarea placeholder="Enter your task here." class="${style.txtArea}" ng-model="app.editedTask.message"></textarea></div>
  </div>
  <div class="row">
    <div class="col-md-2">Urgency</div>
    <div class="col-md-10"><input type="range" ng-model="app.editedTask.urgency" max="10" min="1" step="1" /></div>
  </div>
  <div class="row">
    <div class="col-md-2">Importance</div>
    <div class="col-md-10"><input type="range" ng-model="app.editedTask.importance" max="10" min="1" step="1"/></div>
  </div>
  <div class="ngdialog-buttons pt1">
    <button type="button" class="ngdialog-button ngdialog-button-primary" ng-click="app.saveTask()">Save</button>
    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">Cancel</button>
  </div>
</div>`;
class AppCtrl {
  static $inject = ['$scope', '$element', TaskService, ModalProvider];

  constructor($scope, $element, taskService, modal) {
    this.tasks = [];
    this.modal = modal;
    this.$scope = $scope;
    this.$element = $element;
    this.taskService = taskService;

    angular.element($element).addClass(style.app);

    this.updateTaskList();
  }
  updateTaskList(callback) {
    const promise = this.taskService.getList().then((tasks) => {
      this.tasks = tasks;
    });
    if (callback) {
      promise.then(callback);
    }
  }
  openTask(task) {
    this.editedTask = Task.clone(task) || new Task();

    this.taskEditor = this.modal.open({template: editorTemplate, scope: this.$scope});
    this.taskEditor.closePromise.then(() => {
      // Do memory cleaning when dialog closes
      delete this.taskEditor;
    });
  }
  saveTask() {
    this.updateTask(this.editedTask).then(() => {
      if (this.taskEditor) {
        this.taskEditor.close(1);
      }
    }, (error) => {
        alert(error.message);
    });
  }
  updateTask(task) {
    return this.taskService.saveTask(task).then((task) => {
      let idMatches = this.tasks.filter((item) => { return item.id === task.id });

      if(idMatches.length) {
        idMatches[0].fromObject(task);
      } else {
        this.tasks.push(task);
      }
      this.tasks = [...this.tasks];
    });
  }
}

const MODULE_NAME = 'app';
angular.module(MODULE_NAME, [
  angularRoute,
  ...allServices(),
  ...allComponents(),
  ...allProviders()
]).
config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
    when('/app', {
      template: `
        <app class="cover">
          Loading...
        </app>
      `
    }).
    when('/settings', {
      template: `
        <div>Settings</div>
      `
    }).
    otherwise({
      redirectTo: '/app'
    });
  }]).
directive('app', () => {
  return {
    template,
    controller: AppCtrl,
    controllerAs: 'app'
  }
});

export default MODULE_NAME;




