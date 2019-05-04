import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { AppComponent } from "./component/app/app.component";
import { MovieService } from "./service/movie.service";
import { MovieComponent } from "./component/movie/movie.component";
import { HttpClientModule } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
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

@NgModule({
  declarations: [AppComponent, MovieComponent],
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
    MatExpansionModule
  ],
  providers: [MovieService],
  bootstrap: [AppComponent]
})
export class AppModule {}
