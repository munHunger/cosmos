import { Injectable } from "@angular/core";
import { Observable, throwError } from "rxjs";
import { map, catchError } from "rxjs/operators";
import { HttpClient } from "@angular/common/http";

@Injectable()
export class MovieService {
  constructor(private http: HttpClient) {}

  public popular(): Observable<any> {
    return this.http.get<any>("/api/popular").pipe(
      map((data: any) => {
        return data;
      }),
      catchError(error => {
        return this.handleError(error);
      })
    );
  }

  private handleError(error: Response | any) {
    let errMsg: string;
    errMsg = `${error.status} - ${error.statusText || ""}`;
    console.error(errMsg);
    return throwError(errMsg);
  }
}
