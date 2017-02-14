import { Injectable } from '@angular/core';
import { VideoItem } from '../model/video-item.model';

@Injectable()
export class StuckItemsService {
  getItems(): VideoItem[] {
    return [new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem(), new VideoItem()];
  }
}
