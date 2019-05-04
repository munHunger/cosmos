import { Component, Input } from "@angular/core";
import { MovieService } from "src/app/service/movie.service";
import { Movie } from "src/app/model/movie.model";

@Component({
  selector: "movie",
  templateUrl: "./movie.component.html",
  styleUrls: ["./movie.component.sass"]
})
export class MovieComponent {
  @Input()
  private movie: Movie;
  constructor(private service: MovieService) {}
}
