export class Movie {
  id: number;
  title: String;
  overview: String;
  poster: String;
  backdrop: String;
  year: number;
  genre: String[];
  rating: Rating;
}

export class Rating {
  average: number;
}
