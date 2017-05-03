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
var movie_1 = require("../movie/movie");
var ShortMovieViewComponent = (function () {
    function ShortMovieViewComponent() {
    }
    return ShortMovieViewComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", movie_1.Movie)
], ShortMovieViewComponent.prototype, "movie", void 0);
ShortMovieViewComponent = __decorate([
    core_1.Component({
        selector: 'short-movie-view',
        templateUrl: './short-movie-view.component.html',
        styleUrls: ['./short-movie-view.component.css']
    })
], ShortMovieViewComponent);
exports.ShortMovieViewComponent = ShortMovieViewComponent;
//# sourceMappingURL=short-movie-view.component.js.map