import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { AppComponent } from "./component/app/app.component";
import { MovieService } from "./service/movie.service";
import { MovieComponent } from "./component/movie/movie.component";
import { HttpClientModule } from "@angular/common/http";

@NgModule({
  declarations: [AppComponent, MovieComponent],
  imports: [BrowserModule, HttpClientModule],
  providers: [MovieService],
  bootstrap: [AppComponent]
})
export class AppModule {}
