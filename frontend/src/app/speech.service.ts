import {Injectable, NgZone} from '@angular/core';
import {Subject} from 'rxjs';
import * as SockJS from 'sockjs-client';
import {Gif} from './gif';

declare const annyang: any;
declare const Stomp: any;

@Injectable({
  providedIn: 'root'
})
export class SpeechService {

  _ws$ = new Subject<Gif>();
  errors$ = new Subject<{ [key: string]: any }>();
  listening = false;
  private serverUrl = '/socket';
  private stompClient;

  constructor(private zone: NgZone) {
    this.initializeWebSocketConnection();

  }

  initializeWebSocketConnection() {


    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;

    this.stompClient.connect({}, (frame) => {
      this.sendMessage('Hello');
      this.stompClient.subscribe('/gifs', (message) => {
        if (message.body) {
          console.log('RESPONSE WS ', JSON.parse(message.body));
          this._ws$.next(JSON.parse(message.body));
        }
      });
    });
  }

  sendMessage(message) {
    this.stompClient.send('/app/send/query', {}, message);
  }


  init() {
    // Log anything the user says and what speech recognition thinks it might be
    annyang.addCallback('result', (userSaid) => {
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
