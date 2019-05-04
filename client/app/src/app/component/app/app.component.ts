import { Component } from "@angular/core";
import { MovieService } from "src/app/service/movie.service";
import { Movie } from "src/app/model/movie.model";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.sass"]
})
export class AppComponent {
  title = "cosmos";

  private popular: Movie[] = [];

  constructor(private service: MovieService) {
    service.popular().subscribe(list => {
      this.popular = list;
    });
  }
}
