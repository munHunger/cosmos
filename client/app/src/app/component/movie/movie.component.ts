import { Component, Input, ViewChild, ElementRef } from "@angular/core";
import { MovieService } from "src/app/service/movie.service";
import { Movie } from "src/app/model/movie.model";
import { MatSnackBar } from "@angular/material";
import { Router } from "@angular/router";

@Component({
  selector: "movie",
  templateUrl: "./movie.component.html",
  styleUrls: ["./movie.component.sass"]
})
export class MovieComponent {
  @Input()
  private movie: Movie;
  constructor(
    private service: MovieService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  private wish() {
    this.service.addToWishlist(this.movie.id).subscribe(() =>
      this.snackBar.open(`Added ${this.movie.title} to wishlist`, "", {
        duration: 2000
      })
    );
  }

  private play() {
    document.location.href = "http://localhost:3343/video?id=" + this.movie.id;
  }
}
