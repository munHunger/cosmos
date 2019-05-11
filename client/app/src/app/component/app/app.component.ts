import { Component } from "@angular/core";
import { MovieService } from "src/app/service/movie.service";
import { Movie } from "src/app/model/movie.model";
import { MatDialog } from "@angular/material";
import { SearchDialog } from "../movie/searchDialog/searchDialog.component";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.sass"]
})
export class AppComponent {
  title = "cosmos";

  private searchValue = "";

  constructor(private service: MovieService, private dialog: MatDialog) {}

  private search() {
    console.log("Searching for " + this.searchValue);
    this.service
      .search(this.searchValue)
      .subscribe(result => this.dialog.open(SearchDialog, { data: result }));
  }
}
