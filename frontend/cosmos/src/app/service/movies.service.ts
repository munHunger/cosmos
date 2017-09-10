import { Injectable } from "@angular/core";
import { Http, Response, Headers } from "@angular/http";
import { Movie } from "../model/movie.model";
import { Observable } from "rxjs";


@Injectable()
export class MovieService
{

    private baseURL: string = "https://munhunger.se/movies/api";
    private headers: any;
  
    constructor(private http: Http)
    {
      this.headers = new Headers();
      this.headers.append('Content-Type', 'application/json');
      this.headers.append('Access-Control-Allow-Headers', '*');
    }

    public getRecomendations(): Observable<Movie[]>
    {
        return this.http.get(this.baseURL + "/movies/recomendations").map(res => res.json()).catch(this.handleError);
    }

    public getExtendMovie(movie: Movie): Observable<Movie>
    {
        return this.http.get(this.baseURL + "/movies/" + movie.internal_id).map(res => res.json()).catch(this.handleError);
    }

    
  private handleError(error: Response | any)
  {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response)
    {
      const body = error.json() || '';
      const err = JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    }
    else
    {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}