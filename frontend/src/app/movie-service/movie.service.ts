/**
 * Created by falapen on 2017-06-25.
 */

import {Injectable} from "@angular/core";
import {Movie} from "../movie/movie.component";
import {MOVIES} from "./mock-movies";
import {MovieObject} from "../movie-model/movie.model";
import {Headers, Http} from "@angular/http";
import 'rxjs/add/operator/toPromise';
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";

@Injectable()
export class MovieService {
  private movieServiceURL = "https://munhunger.se/movies/api/movies/recomendations";
  constructor(private http: Http) {}

  getMovies(): Observable<MovieObject[]> {
    return this.http.get(this.movieServiceURL)
      .map(response => response.json())
      .catch(this.handleError);
  }

  handleError(error: any): Observable<any> {
    console.log("Could not load Movies", error);
    return Observable.throw(error);
  }
}
