/**
 * Created by falapen on 2017-06-25.
 */
export class MovieObject {
  internal_id: string;
  image_url: string;
  title: string;
  year: number;
  genre: [string];
  rating: [{
    provider: string,
      rating: number
  }];
  long_info: {
    description: string,
      actors: [string],
      director: [string],
      writer: [string],
      poster_url: string
  };
}
