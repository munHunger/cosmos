import { Component } from "@angular/core";
import { MovieService } from "src/app/service/movie.service";
import { Movie } from "src/app/model/movie.model";

@Component({
  selector: "movie-grid",
  templateUrl: "./movieGrid.component.html",
  styleUrls: ["./movieGrid.component.sass"]
})
export class MovieGridComponent {
  private popular: Movie[] = [];

  constructor(private service: MovieService) {
    service.popular().subscribe(list => {
      this.popular = list;
    });
  }
}
