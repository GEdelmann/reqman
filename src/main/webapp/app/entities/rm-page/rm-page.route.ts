import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { RMPage } from 'app/shared/model/rm-page.model';
import { RMPageService } from './rm-page.service';
import { RMPageComponent } from './rm-page.component';
import { RMPageDetailComponent } from './rm-page-detail.component';
import { RMPageUpdateComponent } from './rm-page-update.component';
import { RMPageDeletePopupComponent } from './rm-page-delete-dialog.component';
import { IRMPage } from 'app/shared/model/rm-page.model';

@Injectable({ providedIn: 'root' })
export class RMPageResolve implements Resolve<IRMPage> {
    constructor(private service: RMPageService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((rMPage: HttpResponse<RMPage>) => rMPage.body);
        }
        return Observable.of(new RMPage());
    }
}

export const rMPageRoute: Routes = [
    {
        path: 'rm-page',
        component: RMPageComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMPage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-page/:id/view',
        component: RMPageDetailComponent,
        resolve: {
            rMPage: RMPageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMPage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-page/new',
        component: RMPageUpdateComponent,
        resolve: {
            rMPage: RMPageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMPage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-page/:id/edit',
        component: RMPageUpdateComponent,
        resolve: {
            rMPage: RMPageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMPage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rMPagePopupRoute: Routes = [
    {
        path: 'rm-page/:id/delete',
        component: RMPageDeletePopupComponent,
        resolve: {
            rMPage: RMPageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMPage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
