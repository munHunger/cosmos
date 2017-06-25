/**
 * Created by falapen on 2017-06-25.
 */

import {Injectable} from "@angular/core";
import {Movie} from "../movie/movie.component";
import {MOVIES} from "./mock-movies";
import {MovieObject} from "../movie-model/movie.model";

@Injectable()
export class MovieService {
  getMovies(): Promise<MovieObject[]> {
    return Promise.resolve(MOVIES);
  }
}
