import angular from 'angular';

export function buildDirective(name, controller, options, dependencies=[]) {
  let _directive = angular.module(name, dependencies)
    .directive(name, () => {
      return {
        ...options,
        controller
      };
    });
  return _directive.name;
}
