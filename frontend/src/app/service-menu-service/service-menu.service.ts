import { Injectable } from '@angular/core';
import { SERVICES } from './mock-service-list';

@Injectable() 
export class ServiceMenuService {
    getAvailServices(): string[] {
        return SERVICES;
    }
}