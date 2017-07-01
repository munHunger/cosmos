/**
 * Created by falapen on 2017-06-25.
 */
import {MovieObject} from "../movie-model/movie.model";

export const MOVIES: MovieObject[] = [
  {internal_id: 'dildo1', image_url: 'https://image.tmdb.org/t/p/w640/gfJGlDaHuWimErCr5Ql0I8x9QSy.jpg', genre: ['Action'], rating: [{provider: 'Rottentomatoes', rating: 87.7}], title: 'Wonder Woman', year: 2017},
  {internal_id: 'dildo2', image_url: 'https://image.tmdb.org/t/p/w640/f8Ng1Sgb3VLiSwAvrfKeQPzvlfr.jpg', genre: ['Action'], rating: [{provider: 'Rottentomatoes', rating: 77.2}], title: 'Transformers: The Last Knight', year: 2017}
];


/*
internal_id: string;
image_url: string;
title: string;
year: number;
genre: [string];
rating: {
  provider: string,
    rating: number
};
long_info: {
  description: string,
    actors: [string],
    director: [string],
    writer: [string],
    poster_url: string
};*/
