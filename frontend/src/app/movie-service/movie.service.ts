import { Injectable } from '@angular/core';
import { Movie } from '../movie/movie';
import { MOVIES } from './mock-movie';

@Injectable()
export class MovieService {
getShortMovieInfo(): Movie {
   return MOVIES[0];
 }
getShortMovieInfoList(): Movie[] {
  return MOVIES;
}

}
