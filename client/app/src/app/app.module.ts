import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { AppComponent } from "./component/app/app.component";
import { MovieService } from "./service/movie.service";
import { MovieGridComponent } from "./component/movie/grid/movieGrid.component";
import { MovieComponent } from "./component/movie/movie.component";
import { HttpClientModule } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule, Routes } from "@angular/router";
import {
  MatGridListModule,
  MatSnackBarModule,
  MatToolbarModule,
  MatSidenavModule,
  MatOptionModule,
  MatSelectModule,
  MatIconModule,
  MatButtonModule,
  MatExpansionModule
} from "@angular/material";
import { MatBadgeModule } from "@angular/material/badge";

const appRoutes: Routes = [
  { path: "grid/:list", component: MovieGridComponent },
  { path: "**", component: MovieGridComponent }
];

@NgModule({
  declarations: [AppComponent, MovieComponent, MovieGridComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatGridListModule,
    MatSnackBarModule,
    MatBadgeModule,
    MatToolbarModule,
    MatSidenavModule,
    MatOptionModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule,
    MatExpansionModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [MovieService],
  bootstrap: [AppComponent]
})
export class AppModule {}
