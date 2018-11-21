import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  NgZone,
  OnDestroy,
  OnInit
} from '@angular/core';
import {SpeechService} from './speech.service';
import {GiphyService} from './giphy.service';
import {switchMap, tap} from "rxjs/operators";
import {Gif} from "./gif";

declare const SpeechKITT: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent implements OnInit, AfterViewInit, OnDestroy {
  gif: Gif;

  constructor(private speechService: SpeechService,
              private ngZone: NgZone,
              private cdRef: ChangeDetectorRef,
              private giphyService: GiphyService) {
    this.speechService.init();
    this.startListening();
  }

  ngOnInit() {
    this.speechService.speechResult$.pipe(
      tap(() => this.gif = null),
      switchMap(text => this.giphyService.searchGif(text)),
    ).subscribe((gif: Gif) => {
      console.log({...gif});
      this.gif = gif;
      this.cdRef.detectChanges();
    });
  }

  ngOnDestroy() {
    this.speechService.abort();
  }

  startListening(): void {
    this.speechService.startListening();
  }

  ngAfterViewInit(): void {
    // Tell KITT to use annyang
    SpeechKITT.annyang();

    // Define a stylesheet for KITT to use

    // Render KITT's interface
    SpeechKITT.vroom();

  }
}
