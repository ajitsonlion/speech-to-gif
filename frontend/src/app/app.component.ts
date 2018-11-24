import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {SpeechToGifService} from './speech-to-gif.service';
import {Gif} from './gif';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent implements OnInit, OnDestroy {
  gif: Gif;

  loaded$: Observable<boolean>;

  imageLoaded = false;

  constructor(private speechToGifService: SpeechToGifService,
              private cdRef: ChangeDetectorRef) {
    this.speechToGifService.init();
    this.startListening();
  }

  ngOnInit() {
    this.speechToGifService._ws$.asObservable().subscribe((gif: Gif) => {
      this.gif = gif;
      this.imageLoaded = false;
      //  console.log(this.gif, this.imageLoaded);
      console.log(this.gif);
      this.cdRef.detectChanges();
    });
    this.loaded$ = this.speechToGifService.loading$.pipe(map(loaded => !loaded));
  }

  ngOnDestroy() {
    this.speechToGifService.abort();
  }

  startListening(): void {
    this.speechToGifService.startListening();
  }

  load(e) {
    console.log(e);
    this.imageLoaded = true;

    // this.speechToGifService.stopLoading();
  }

  reload() {
    this.speechToGifService.sendMessage(this.gif.fullText);

  }
}
