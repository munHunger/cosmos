import { Component } from "@angular/core";
import { Movie } from "src/app/model/movie.model";
import { MovieService } from "src/app/service/movie.service";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "movie-details",
  templateUrl: "./movieDetails.component.html",
  styleUrls: ["./movieDetails.component.sass"]
})
export class MovieDetailsComponent {
  private movie: Movie;

  constructor(private service: MovieService, private route: ActivatedRoute) {
    this.route.paramMap.subscribe(params => {
      let id = params.get("id");

      service.getSingle(parseInt(id)).subscribe(movie => {
        this.movie = movie;
      });
    });
  }
}
