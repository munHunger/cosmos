import { Component, OnInit } from '@angular/core';
import { MovieList } from '../movie-list/movie-list.component';
import { ServiceMenuService } from '../service-menu-service/service-menu.service';

@Component({
    selector: 'service-menu', 
    templateUrl: './service-menu.component.html',
    styleUrls: ['./service-menu.component.css'], 
    providers: [ServiceMenuService]
})

export class ServiceMenu implements OnInit{
    availServiceViews: string[];
    constructor(private serviceMenuService: ServiceMenuService) {}

    ngOnInit(): void {
        this.getServiceList();
    }

    getServiceList(): void {
        this.availServiceViews = this.serviceMenuService.getAvailServices();
    }

}