import { Injectable } from "@angular/core";
import { Http, Response, Headers } from "@angular/http";
import { Setting } from '../model/setting.model';
import { Observable } from "rxjs";


@Injectable()
export class SettingsService
{

    private baseURL: string = "https://munhunger.se/settings/api";
    private headers: any;
  
    constructor(private http: Http)
    {
      this.headers = new Headers();
      this.headers.append('Content-Type', 'application/json');
      this.headers.append('Access-Control-Allow-Headers', '*');
    }

    public getSettings(): Setting[]
    {
        let dbURL = new Setting();
        dbURL.name = "movie_db_api_uri";
        dbURL.regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        dbURL.type = "string";
        dbURL.value = "test_url";
        let dbKey = new Setting();
        dbKey.name = "movie_db_api_key";
        dbKey.regex = ".*";
        dbKey.type = "string";
        let movieSetting = new Setting();
        movieSetting.name = "movies";
        movieSetting.children = [dbKey, dbURL];
        return [movieSetting, movieSetting];
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