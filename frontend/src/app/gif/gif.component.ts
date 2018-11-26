import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Gif} from '../gif';
import {SpeechToGifService} from '../speech-to-gif.service';

@Component({
  selector: 'app-gif',
  templateUrl: './gif.component.html',
  styleUrls: ['./gif.component.scss'],
})
export class GifComponent implements OnInit {

  gif: Gif;
  imageLoaded = false;

  constructor(private activatedRoute: ActivatedRoute,
              private speechToGifService: SpeechToGifService) {
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(q => {
      this.gif = <Gif>{...q};
      this.imageLoaded = false;
      console.log(this.gif);
    });
  }

  onImageLoad(e: Event) {
    this.imageLoaded = true;
  }

  reload() {
    this.speechToGifService.sendMessage(this.gif.fullText);
  }
}
