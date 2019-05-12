import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { Movie } from "src/app/model/movie.model";
import { Router } from "@angular/router";

@Component({
  selector: "search-dialog",
  templateUrl: "./searchDialog.component.html",
  styleUrls: ["./searchDialog.component.sass"]
})
export class SearchDialog {
  constructor(
    private router: Router,
    public dialogRef: MatDialogRef<SearchDialog>,
    @Inject(MAT_DIALOG_DATA) public data: Movie[]
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  select(movie: Movie): void {
    this.dialogRef.close();
    this.router.navigate(["movie", movie.id]);
  }
}
