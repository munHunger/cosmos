"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var service_menu_service_1 = require("../service-menu-service/service-menu.service");
var ServiceMenu = (function () {
    function ServiceMenu(serviceMenuService) {
        this.serviceMenuService = serviceMenuService;
    }
    ServiceMenu.prototype.ngOnInit = function () {
        this.getServiceList();
    };
    ServiceMenu.prototype.getServiceList = function () {
        this.availServiceViews = this.serviceMenuService.getAvailServices();
    };
    return ServiceMenu;
}());
ServiceMenu = __decorate([
    core_1.Component({
        selector: 'service-menu',
        templateUrl: './service-menu.component.html',
        styleUrls: ['./service-menu.component.css'],
        providers: [service_menu_service_1.ServiceMenuService]
    }),
    __metadata("design:paramtypes", [service_menu_service_1.ServiceMenuService])
], ServiceMenu);
exports.ServiceMenu = ServiceMenu;
//# sourceMappingURL=service-menu.component.js.map