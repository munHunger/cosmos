import { Component } from '@angular/core';
import { VideoItem } from '../model/video-item.model';

@Component({
    selector: 'video-item',
    moduleId: module.id,
    templateUrl: 'video-item.component.html'
})
export class VideoItemCard {
  item: VideoItem;
}
