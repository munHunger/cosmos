import { Component } from "@angular/core";
import { MovieService } from "src/app/service/movie.service";
import { Movie } from "src/app/model/movie.model";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "movie-grid",
  templateUrl: "./movieGrid.component.html",
  styleUrls: ["./movieGrid.component.sass"]
})
export class MovieGridComponent {
  private list: Movie[] = [];

  constructor(private service: MovieService, private route: ActivatedRoute) {
    this.route.paramMap.subscribe(params => {
      let listName = params.get("list");
      switch (listName) {
        case "wishlist":
          service.library().subscribe(list => {
            this.list = list;
          });
          break;
        default:
        case "popular":
          service.popular().subscribe(list => {
            this.list = list;
          });
          break;
      }
    });
  }
}
