import ngDialog from 'ng-dialog';
import { buildService } from "./../common/Service";

class ModalProvider {
  static $inject = [ngDialog];

  constructor(ngDialog) {
    this.ngDialog = ngDialog;
  }
  confirm(template = '', options = ['ok', 'cancel']) {
    // Returns a promise to confirm box
    let buttons = options.map((option) => {
      if (option === "ok") {
        return '<button type="button" class="ngdialog-button ngdialog-button-primary" ng-click="confirm(1)">Ok</button>';
      }else if (option === "yes") {
        return '<button type="button" class="ngdialog-button ngdialog-button-primary" ng-click="confirm(1)">Yes</button>';
      }else if (option === "save") {
        return '<button type="button" class="ngdialog-button ngdialog-button-primary" ng-click="confirm(1)">Save</button>';
      }else if (option === "cancel") {
        return '<button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">Cancel</button>';
      }else if (option === "no") {
        return '<button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">No</button>';
      }
    });
    if (buttons.length) {
      buttons = `<div class="ngdialog-buttons">
                    ${buttons.join("\n")}
                </div>`;
    } else {
      buttons = '';
    }
    return this.ngDialog.openConfirm({
      template: `${template} ${buttons}`,
      className: 'ngdialog-theme-default',
      plain: true
    });
  }
  open(options) {
    return this.ngDialog.open({
      plain: true,
      className: 'ngdialog-theme-default',
      ...options
    });
  }
}

export default buildService('modalProvider', ModalProvider, [ngDialog]);
