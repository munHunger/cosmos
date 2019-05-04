(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["main"],{

/***/ "./src/$$_lazy_route_resource lazy recursive":
/*!**********************************************************!*\
  !*** ./src/$$_lazy_route_resource lazy namespace object ***!
  \**********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncaught exception popping up in devtools
	return Promise.resolve().then(function() {
		var e = new Error("Cannot find module '" + req + "'");
		e.code = 'MODULE_NOT_FOUND';
		throw e;
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = "./src/$$_lazy_route_resource lazy recursive";

/***/ }),

/***/ "./src/app/app.module.ts":
/*!*******************************!*\
  !*** ./src/app/app.module.ts ***!
  \*******************************/
/*! exports provided: AppModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppModule", function() { return AppModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/platform-browser */ "./node_modules/@angular/platform-browser/fesm5/platform-browser.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _component_app_app_component__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./component/app/app.component */ "./src/app/component/app/app.component.ts");
/* harmony import */ var _service_movie_service__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./service/movie.service */ "./src/app/service/movie.service.ts");
/* harmony import */ var _component_movie_movie_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./component/movie/movie.component */ "./src/app/component/movie/movie.component.ts");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm5/http.js");
/* harmony import */ var _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @angular/platform-browser/animations */ "./node_modules/@angular/platform-browser/fesm5/animations.js");
/* harmony import */ var _angular_material__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! @angular/material */ "./node_modules/@angular/material/esm5/material.es5.js");
/* harmony import */ var _angular_material_badge__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! @angular/material/badge */ "./node_modules/@angular/material/esm5/badge.es5.js");










var AppModule = /** @class */ (function () {
    function AppModule() {
    }
    AppModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_2__["NgModule"])({
            declarations: [_component_app_app_component__WEBPACK_IMPORTED_MODULE_3__["AppComponent"], _component_movie_movie_component__WEBPACK_IMPORTED_MODULE_5__["MovieComponent"]],
            imports: [
                _angular_platform_browser__WEBPACK_IMPORTED_MODULE_1__["BrowserModule"],
                _angular_common_http__WEBPACK_IMPORTED_MODULE_6__["HttpClientModule"],
                _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_7__["BrowserAnimationsModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatGridListModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatSnackBarModule"],
                _angular_material_badge__WEBPACK_IMPORTED_MODULE_9__["MatBadgeModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatToolbarModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatSidenavModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatOptionModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatSelectModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatIconModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatButtonModule"],
                _angular_material__WEBPACK_IMPORTED_MODULE_8__["MatExpansionModule"]
            ],
            providers: [_service_movie_service__WEBPACK_IMPORTED_MODULE_4__["MovieService"]],
            bootstrap: [_component_app_app_component__WEBPACK_IMPORTED_MODULE_3__["AppComponent"]]
        })
    ], AppModule);
    return AppModule;
}());



/***/ }),

/***/ "./src/app/component/app/app.component.html":
/*!**************************************************!*\
  !*** ./src/app/component/app/app.component.html ***!
  \**************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<mat-toolbar color=\"primary\">\n  <mat-toolbar-row>\n    <button mat-button (click)=\"drawer.toggle()\">\n      <mat-icon>menu</mat-icon>\n    </button>\n    <span>COSMOS</span>\n  </mat-toolbar-row>\n</mat-toolbar>\n<mat-drawer-container class=\"example-container\" hasBackdrop=\"false\">\n  <mat-drawer #drawer mode=\"side\">\n    <mat-expansion-panel>\n      <mat-expansion-panel-header>\n        <mat-panel-title>\n          Movies\n        </mat-panel-title>\n      </mat-expansion-panel-header>\n      <p><button mat-button>Popular</button></p>\n      <p><button mat-button>My List</button></p>\n    </mat-expansion-panel>\n  </mat-drawer>\n  <mat-drawer-content>\n    <mat-grid-list cols=\"5\" rowHeight=\"3:5\">\n      <mat-grid-tile *ngFor=\"let movie of popular\">\n        <movie [movie]=\"movie\"></movie\n      ></mat-grid-tile>\n    </mat-grid-list>\n  </mat-drawer-content>\n</mat-drawer-container>\n"

/***/ }),

/***/ "./src/app/component/app/app.component.sass":
/*!**************************************************!*\
  !*** ./src/app/component/app/app.component.sass ***!
  \**************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = ":host {\n  position: absolute;\n  left: 0px;\n  top: 0px;\n  width: 100%; }\n\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi9ob21lL211bmh1bmdlci9kZXZlbG9wL2Nvc21vcy9jbGllbnQvYXBwL3NyYy9hcHAvY29tcG9uZW50L2FwcC9hcHAuY29tcG9uZW50LnNhc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUM7RUFDRyxrQkFBa0I7RUFDbEIsU0FBUztFQUNULFFBQVE7RUFDUixXQUFXLEVBQUEiLCJmaWxlIjoic3JjL2FwcC9jb21wb25lbnQvYXBwL2FwcC5jb21wb25lbnQuc2FzcyIsInNvdXJjZXNDb250ZW50IjpbIiA6aG9zdCB7XG4gICAgcG9zaXRpb246IGFic29sdXRlO1xuICAgIGxlZnQ6IDBweDtcbiAgICB0b3A6IDBweDtcbiAgICB3aWR0aDogMTAwJTsgfVxuIl19 */"

/***/ }),

/***/ "./src/app/component/app/app.component.ts":
/*!************************************************!*\
  !*** ./src/app/component/app/app.component.ts ***!
  \************************************************/
/*! exports provided: AppComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppComponent", function() { return AppComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var src_app_service_movie_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/service/movie.service */ "./src/app/service/movie.service.ts");



var AppComponent = /** @class */ (function () {
    function AppComponent(service) {
        var _this = this;
        this.service = service;
        this.title = "cosmos";
        this.popular = [];
        service.popular().subscribe(function (list) {
            _this.popular = list;
        });
    }
    AppComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            selector: "app-root",
            template: __webpack_require__(/*! ./app.component.html */ "./src/app/component/app/app.component.html"),
            styles: [__webpack_require__(/*! ./app.component.sass */ "./src/app/component/app/app.component.sass")]
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [src_app_service_movie_service__WEBPACK_IMPORTED_MODULE_2__["MovieService"]])
    ], AppComponent);
    return AppComponent;
}());



/***/ }),

/***/ "./src/app/component/movie/movie.component.html":
/*!******************************************************!*\
  !*** ./src/app/component/movie/movie.component.html ***!
  \******************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<div class=\"container\" (click)=\"select()\">\n  <div class=\"rating\">\n    {{ movie.rating.average }}\n  </div>\n  <img [src]=\"movie.poster\" class=\"poster\" />\n  <div class=\"title\">\n    {{ movie.title }}\n  </div>\n  <div class=\"genre\">\n    {{ movie.genre.join(\" \") }}\n  </div>\n</div>\n"

/***/ }),

/***/ "./src/app/component/movie/movie.component.sass":
/*!******************************************************!*\
  !*** ./src/app/component/movie/movie.component.sass ***!
  \******************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = ".poster {\n  width: 100%;\n  box-shadow: 0px 0px 10px 1px rgba(0, 0, 0, 0.5);\n  cursor: pointer;\n  transition: all 0.2s; }\n\n.poster:hover {\n  box-shadow: 0px 0px 15px 3px rgba(0, 0, 0, 0.5); }\n\n.container {\n  padding: 15px;\n  position: relative; }\n\n.title {\n  margin-top: 10px;\n  color: rgba(255, 255, 255, 0.8);\n  text-align: left;\n  font-weight: 700;\n  font-size: 16px;\n  margin-left: 20px; }\n\n.genre {\n  text-align: left;\n  font-size: 12px;\n  color: rgba(255, 255, 255, 0.4);\n  margin-left: 20px; }\n\n.rating {\n  position: absolute;\n  right: 15px;\n  padding: 10px;\n  background-color: rgba(255, 255, 255, 0.3);\n  font-size: 18px;\n  font-weight: 900;\n  color: rgba(255, 255, 255, 0.9);\n  box-shadow: -2px 2px 4px 0px rgba(0, 0, 0, 0.3);\n  text-shadow: -1px -1px 0 rgba(0, 0, 0, 0.3), 1px -1px 0 rgba(0, 0, 0, 0.3), -1px 1px 0 rgba(0, 0, 0, 0.3), 1px 1px 0 rgba(0, 0, 0, 0.3); }\n\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi9ob21lL211bmh1bmdlci9kZXZlbG9wL2Nvc21vcy9jbGllbnQvYXBwL3NyYy9hcHAvY29tcG9uZW50L21vdmllL21vdmllLmNvbXBvbmVudC5zYXNzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUNBO0VBQ0ksV0FBVztFQUNYLCtDQUE0QztFQUM1QyxlQUFlO0VBQ2Ysb0JBQW9CLEVBQUE7O0FBRXhCO0VBQ0ksK0NBQTRDLEVBQUE7O0FBRWhEO0VBQ0ksYUFYVTtFQVlWLGtCQUFrQixFQUFBOztBQUV0QjtFQUNJLGdCQUFnQjtFQUNoQiwrQkFBNEI7RUFDNUIsZ0JBQWdCO0VBQ2hCLGdCQUFnQjtFQUNoQixlQUFlO0VBQ2YsaUJBQWlCLEVBQUE7O0FBRXJCO0VBQ0ksZ0JBQWdCO0VBQ2hCLGVBQWU7RUFDZiwrQkFBNEI7RUFDNUIsaUJBQWlCLEVBQUE7O0FBRXJCO0VBQ0ksa0JBQWtCO0VBQ2xCLFdBOUJVO0VBK0JWLGFBQWE7RUFDYiwwQ0FBdUM7RUFDdkMsZUFBZTtFQUNmLGdCQUFnQjtFQUNoQiwrQkFBNEI7RUFDNUIsK0NBQTRDO0VBRTVDLHVJQUR5QixFQUFBIiwiZmlsZSI6InNyYy9hcHAvY29tcG9uZW50L21vdmllL21vdmllLmNvbXBvbmVudC5zYXNzIiwic291cmNlc0NvbnRlbnQiOlsiJHBhZGRpbmc6IDE1cHg7XG4ucG9zdGVyIHtcbiAgICB3aWR0aDogMTAwJTtcbiAgICBib3gtc2hhZG93OiAwcHggMHB4IDEwcHggMXB4IHJnYmEoMCwwLDAsMC41KTtcbiAgICBjdXJzb3I6IHBvaW50ZXI7XG4gICAgdHJhbnNpdGlvbjogYWxsIDAuMnM7IH1cblxuLnBvc3Rlcjpob3ZlciB7XG4gICAgYm94LXNoYWRvdzogMHB4IDBweCAxNXB4IDNweCByZ2JhKDAsMCwwLDAuNSk7IH1cblxuLmNvbnRhaW5lciB7XG4gICAgcGFkZGluZzogJHBhZGRpbmc7XG4gICAgcG9zaXRpb246IHJlbGF0aXZlOyB9XG5cbi50aXRsZSB7XG4gICAgbWFyZ2luLXRvcDogMTBweDtcbiAgICBjb2xvcjogcmdiYSgyNTUsMjU1LDI1NSwwLjgpO1xuICAgIHRleHQtYWxpZ246IGxlZnQ7XG4gICAgZm9udC13ZWlnaHQ6IDcwMDtcbiAgICBmb250LXNpemU6IDE2cHg7XG4gICAgbWFyZ2luLWxlZnQ6IDIwcHg7IH1cblxuLmdlbnJlIHtcbiAgICB0ZXh0LWFsaWduOiBsZWZ0O1xuICAgIGZvbnQtc2l6ZTogMTJweDtcbiAgICBjb2xvcjogcmdiYSgyNTUsMjU1LDI1NSwwLjQpO1xuICAgIG1hcmdpbi1sZWZ0OiAyMHB4OyB9XG5cbi5yYXRpbmcge1xuICAgIHBvc2l0aW9uOiBhYnNvbHV0ZTtcbiAgICByaWdodDogJHBhZGRpbmc7XG4gICAgcGFkZGluZzogMTBweDtcbiAgICBiYWNrZ3JvdW5kLWNvbG9yOiByZ2JhKDI1NSwyNTUsMjU1LDAuMyk7XG4gICAgZm9udC1zaXplOiAxOHB4O1xuICAgIGZvbnQtd2VpZ2h0OiA5MDA7XG4gICAgY29sb3I6IHJnYmEoMjU1LDI1NSwyNTUsMC45KTtcbiAgICBib3gtc2hhZG93OiAtMnB4IDJweCA0cHggMHB4IHJnYmEoMCwwLDAsMC4zKTtcbiAgICAkb3V0bGluZTogcmdiYSgwLDAsMCwwLjMpO1xuICAgIHRleHQtc2hhZG93OiAtMXB4IC0xcHggMCAkb3V0bGluZSwgMXB4IC0xcHggMCAkb3V0bGluZSwgLTFweCAxcHggMCAkb3V0bGluZSwgMXB4IDFweCAwICRvdXRsaW5lOyB9XG4iXX0= */"

/***/ }),

/***/ "./src/app/component/movie/movie.component.ts":
/*!****************************************************!*\
  !*** ./src/app/component/movie/movie.component.ts ***!
  \****************************************************/
/*! exports provided: MovieComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MovieComponent", function() { return MovieComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var src_app_service_movie_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/service/movie.service */ "./src/app/service/movie.service.ts");
/* harmony import */ var src_app_model_movie_model__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/model/movie.model */ "./src/app/model/movie.model.ts");
/* harmony import */ var _angular_material__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/material */ "./node_modules/@angular/material/esm5/material.es5.js");





var MovieComponent = /** @class */ (function () {
    function MovieComponent(service, snackBar) {
        this.service = service;
        this.snackBar = snackBar;
    }
    MovieComponent.prototype.select = function () {
        console.log("an attempt was made");
        this.snackBar.open("Hello! :)", "ok", { duration: 2000 });
    };
    tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Input"])(),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:type", src_app_model_movie_model__WEBPACK_IMPORTED_MODULE_3__["Movie"])
    ], MovieComponent.prototype, "movie", void 0);
    MovieComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            selector: "movie",
            template: __webpack_require__(/*! ./movie.component.html */ "./src/app/component/movie/movie.component.html"),
            styles: [__webpack_require__(/*! ./movie.component.sass */ "./src/app/component/movie/movie.component.sass")]
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [src_app_service_movie_service__WEBPACK_IMPORTED_MODULE_2__["MovieService"], _angular_material__WEBPACK_IMPORTED_MODULE_4__["MatSnackBar"]])
    ], MovieComponent);
    return MovieComponent;
}());



/***/ }),

/***/ "./src/app/model/movie.model.ts":
/*!**************************************!*\
  !*** ./src/app/model/movie.model.ts ***!
  \**************************************/
/*! exports provided: Movie, Rating */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Movie", function() { return Movie; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Rating", function() { return Rating; });
var Movie = /** @class */ (function () {
    function Movie() {
    }
    return Movie;
}());

var Rating = /** @class */ (function () {
    function Rating() {
    }
    return Rating;
}());



/***/ }),

/***/ "./src/app/service/movie.service.ts":
/*!******************************************!*\
  !*** ./src/app/service/movie.service.ts ***!
  \******************************************/
/*! exports provided: MovieService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MovieService", function() { return MovieService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! rxjs */ "./node_modules/rxjs/_esm5/index.js");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! rxjs/operators */ "./node_modules/rxjs/_esm5/operators/index.js");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm5/http.js");





var MovieService = /** @class */ (function () {
    function MovieService(http) {
        this.http = http;
    }
    MovieService.prototype.popular = function () {
        var _this = this;
        return this.http.get("/api/popular").pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["map"])(function (data) { return data.movie; }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_3__["catchError"])(function (error) {
            return _this.handleError(error);
        }));
    };
    MovieService.prototype.handleError = function (error) {
        var errMsg;
        errMsg = error.status + " - " + (error.statusText || "");
        console.error(errMsg);
        return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(errMsg);
    };
    MovieService = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])(),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_angular_common_http__WEBPACK_IMPORTED_MODULE_4__["HttpClient"]])
    ], MovieService);
    return MovieService;
}());



/***/ }),

/***/ "./src/environments/environment.ts":
/*!*****************************************!*\
  !*** ./src/environments/environment.ts ***!
  \*****************************************/
/*! exports provided: environment */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "environment", function() { return environment; });
// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
var environment = {
    production: false
};
/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.


/***/ }),

/***/ "./src/main.ts":
/*!*********************!*\
  !*** ./src/main.ts ***!
  \*********************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_platform_browser_dynamic__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/platform-browser-dynamic */ "./node_modules/@angular/platform-browser-dynamic/fesm5/platform-browser-dynamic.js");
/* harmony import */ var _app_app_module__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./app/app.module */ "./src/app/app.module.ts");
/* harmony import */ var _environments_environment__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./environments/environment */ "./src/environments/environment.ts");




if (_environments_environment__WEBPACK_IMPORTED_MODULE_3__["environment"].production) {
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_0__["enableProdMode"])();
}
Object(_angular_platform_browser_dynamic__WEBPACK_IMPORTED_MODULE_1__["platformBrowserDynamic"])().bootstrapModule(_app_app_module__WEBPACK_IMPORTED_MODULE_2__["AppModule"])
    .catch(function (err) { return console.error(err); });


/***/ }),

/***/ 0:
/*!***************************!*\
  !*** multi ./src/main.ts ***!
  \***************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(/*! /home/munhunger/develop/cosmos/client/app/src/main.ts */"./src/main.ts");


/***/ })

},[[0,"runtime","vendor"]]]);
//# sourceMappingURL=main.js.map