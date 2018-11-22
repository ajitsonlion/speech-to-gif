import {ChangeDetectionStrategy, ChangeDetectorRef, Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {SpeechService} from './speech.service';
import {GiphyService} from './giphy.service';
import {switchMap, tap} from 'rxjs/operators';
import {Gif} from './gif';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent implements OnInit, OnDestroy {
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
      tap(() => {
        this.gif = null;
        this.cdRef.detectChanges();
      }),
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

}
