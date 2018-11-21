import {Injectable, NgZone} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';
import {filter} from "rxjs/operators";

declare const annyang: any;
declare const SpeechKITT: any;

@Injectable({
  providedIn: 'root'
})
export class SpeechService {

  private _speechResult$ = new BehaviorSubject<string[]>(null);
  errors$ = new Subject<{ [key: string]: any }>();
  listening = false;

  constructor(private zone: NgZone) {
  }

  get speechResult$() {
    return this._speechResult$.asObservable().pipe(filter(result => !!result));
  }

  init() {
    // Log anything the user says and what speech recognition thinks it might be
    annyang.addCallback('result', (userSaid) => {
      this._speechResult$.next(userSaid);
    });
    annyang.addCallback('errorNetwork', (err) => {
      this._handleError('network', 'A network error occurred.', err);
    });
    annyang.addCallback('errorPermissionBlocked', (err) => {
      this._handleError('blocked', 'Browser blocked microphone permissions.', err);
    });
    annyang.addCallback('errorPermissionDenied', (err) => {
      this._handleError('denied', 'User denied microphone permissions.', err);
    });
    annyang.addCallback('resultNoMatch', (userSaid) => {
      this._handleError(
        'no match',
        'Spoken command not recognized.',
        {results: userSaid});
    });
  }


  private _handleError(error, msg, obj) {
    this.zone.run(() => {
      this.errors$.next({
        error,
        msg,
        obj
      });
    });
  }

  startListening() {
    annyang.start();
    this.listening = true;
  }

  abort() {
    annyang.abort();
    this.listening = false;
  }

}
