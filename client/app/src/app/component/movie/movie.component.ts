import { Component, Input, ViewChild, ElementRef } from "@angular/core";
import { MovieService } from "src/app/service/movie.service";
import { Movie } from "src/app/model/movie.model";
import { MatSnackBar } from "@angular/material";

@Component({
  selector: "movie",
  templateUrl: "./movie.component.html",
  styleUrls: ["./movie.component.sass"]
})
export class MovieComponent {
  @Input()
  private movie: Movie;
  constructor(private service: MovieService, private snackBar: MatSnackBar) {}

  private select() {
    console.log("an attempt was made");
    this.snackBar.open("Hello! :)", "ok", { duration: 2000 });
  }
}
