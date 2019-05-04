import { Component } from "@angular/core";
import { MovieService } from "src/app/service/movie.service";

@Component({
  selector: "movie",
  templateUrl: "./movie.component.html",
  styleUrls: ["./movie.component.sass"]
})
export class MovieComponent {
  constructor(private service: MovieService) {
    service.popular().subscribe(list => {
      console.log(list);
    });
  }
}
