import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { AppComponent } from "./component/app/app.component";
import { MovieService } from "./service/movie.service";
import { MovieComponent } from "./component/movie/movie.component";
import { HttpClientModule } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatGridListModule, MatSnackBarModule } from "@angular/material";

@NgModule({
  declarations: [AppComponent, MovieComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatGridListModule,
    MatSnackBarModule
  ],
  providers: [MovieService],
  bootstrap: [AppComponent]
})
export class AppModule {}
