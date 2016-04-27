import angular from 'angular';

export function buildService(name, controller, dependencies=[]) {
  if (controller.$inject === undefined) {
    controller.$inject = [];
  }
  let _directive = angular.module(name, dependencies)
    .factory(name, [...controller.$inject, (...args) => {
      return new controller(...args);
    }]);
  return _directive.name;
}

