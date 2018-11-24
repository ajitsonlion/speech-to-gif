import {Injectable, NgZone} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';
import * as SockJS from 'sockjs-client';
import {Gif} from './gif';
import {distinctUntilChanged} from 'rxjs/operators';

declare const annyang: any;
declare const Stomp: any;

@Injectable({
  providedIn: 'root'
})
export class SpeechToGifService {

  _ws$ = new Subject<Gif>();
  private _loading$ = new BehaviorSubject<boolean>(false);
  errors$ = new Subject<{ [key: string]: any }>();
  listening = false;
  private serverUrl = '/socket';
  private stompClient;

  constructor(private zone: NgZone) {
    this.initializeWebSocketConnection();
  }

  get loading$() {
    return this._loading$.asObservable().pipe(distinctUntilChanged());
  }

  startLoading() {
    this._loading$.next(true);
  }

  stopLoading() {
    this._loading$.next(false);
  }

  initializeWebSocketConnection() {
    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;

    this.stompClient.connect({}, (frame) => {
      this.sendMessage('Hello');
      this.stompClient.subscribe('/gifs', (message) => {
        if (message.body) {
          //   console.log('RESPONSE WS ', JSON.parse(message.body));
          this.stopLoading();
          this._ws$.next(JSON.parse(message.body));
        }
      });
    });
  }

  sendMessage(message) {
    this.startLoading();
    this.stompClient.send('/app/send/query', {}, message);
  }


  init() {
    // Log anything the user says and what speech recognition thinks it might be
    annyang.addCallback('result', (userSaid) => {
      console.log(userSaid);
      this.sendMessage(userSaid);
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
