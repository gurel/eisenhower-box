import { buildDirective } from '../../common/Directive';

let template = `
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Eisenhover Box</a>
    </div>

    <div class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li ng-class="{ active: isActivePath('/app')      }"><a href="#/app">App</a></li>
        <li ng-class="{ active: isActivePath('/settings') }"><a href="#/settings">Settings</a></li>
      </ul>
    <p class="navbar-text navbar-right"><a href="/logout" class="navbar-link">Logout</a></p>
    </div>
  </div>
</nav>
`;

class NavBarController {
  static $inject = ['$scope', '$location']
  constructor($scope, $location) {
    this.$scope = $scope;
    this.$location = $location;

    this.link();
  }
  link() {
    this.$scope.isActivePath = this.isActivePath.bind(this);
  }
  isActivePath(viewLocation) {
    console.log(viewLocation, this.$location.path());
    return viewLocation === this.$location.path();
  };
}

export default buildDirective(
  'navbar', NavBarController, {
    restrict: 'E',
    transclude: true,
    template
  });
