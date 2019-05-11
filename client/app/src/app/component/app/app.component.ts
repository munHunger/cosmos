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

  private searchValue = "";

  constructor(private service: MovieService) {}

  private search() {
    console.log("Searching for " + this.searchValue);
    this.service
      .search(this.searchValue)
      .subscribe(result => console.log(result));
  }
}
