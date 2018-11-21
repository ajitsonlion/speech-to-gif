import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Gif} from "./gif";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class GiphyService {


  constructor(private http: HttpClient) {
  }

  searchGif(texts: string[]): Observable<Gif> {
    return this.http.post<Gif>(`/api/giphy/query`, {texts}).pipe(
      catchError(e => {
        console.error('Error whole request');
        return of(null);
      })
    );

  }


}
