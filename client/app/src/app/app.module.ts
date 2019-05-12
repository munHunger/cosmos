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
  MatExpansionModule,
  MatInputModule,
  MatFormFieldModule,
  MatDialogModule
} from "@angular/material";
import { MatBadgeModule } from "@angular/material/badge";
import { FormsModule } from "@angular/forms";
import { SearchDialog } from "./component/movie/searchDialog/searchDialog.component";
import { MovieDetailsComponent } from "./component/movie/details/movieDetails.component";

const appRoutes: Routes = [
  { path: "grid/:list", component: MovieGridComponent },
  { path: "movie/:id", component: MovieDetailsComponent },
  { path: "**", component: MovieGridComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    MovieComponent,
    MovieGridComponent,
    MovieDetailsComponent,
    SearchDialog
  ],
  imports: [
    BrowserModule,
    FormsModule,
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
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    RouterModule.forRoot(appRoutes)
  ],
  entryComponents: [SearchDialog],
  providers: [MovieService],
  bootstrap: [AppComponent]
})
export class AppModule {}
