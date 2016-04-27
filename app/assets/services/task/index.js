import { buildService } from '../../common/Service';
import Task from './Task';

class TaskService {
  static $inject = ['$http', '$q'];

  constructor($http, $q) {
    this.$http = $http;
    this.$q = $q;
  }
  getById(taskID) {
    const defered = this.$q.defer();

    this.$http.get(`task/${taskID}`).then((response) => {
      defered.resolve(new Task().fromObject(response));
    }, (error) => {
      defered.reject(new Error(error));
    });

    return defered.promise;
  }
  getList() {
    const defered = this.$q.defer();

    this.$http.get(`task`).then((response) => {
      const result = response.data.map((item) => {
        return new Task().fromObject(item);
      });
      defered.resolve(result);
    }, (error) => {
      defered.reject(new Error(error));
    });

    return defered.promise;
  }
  saveTask(task) {
    const defered = this.$q.defer();

    if (task.message) {
      this.$http.post(`task`, task.toObject()).then((response) => {
        defered.resolve(task.fromObject(response.data));
      }, (error) => {
        defered.reject(new Error(error));
      });
    } else {
      defered.reject(new Error("Message cannot be empty"));
    }
    
    return defered.promise;
  }
}

export default buildService('taskService', TaskService);
