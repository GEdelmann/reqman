import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { RMCategory } from 'app/shared/model/rm-category.model';
import { RMCategoryService } from './rm-category.service';
import { RMCategoryComponent } from './rm-category.component';
import { RMCategoryDetailComponent } from './rm-category-detail.component';
import { RMCategoryUpdateComponent } from './rm-category-update.component';
import { RMCategoryDeletePopupComponent } from './rm-category-delete-dialog.component';
import { IRMCategory } from 'app/shared/model/rm-category.model';

@Injectable({ providedIn: 'root' })
export class RMCategoryResolve implements Resolve<IRMCategory> {
    constructor(private service: RMCategoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((rMCategory: HttpResponse<RMCategory>) => rMCategory.body);
        }
        return Observable.of(new RMCategory());
    }
}

export const rMCategoryRoute: Routes = [
    {
        path: 'rm-category',
        component: RMCategoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-category/:id/view',
        component: RMCategoryDetailComponent,
        resolve: {
            rMCategory: RMCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-category/new',
        component: RMCategoryUpdateComponent,
        resolve: {
            rMCategory: RMCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-category/:id/edit',
        component: RMCategoryUpdateComponent,
        resolve: {
            rMCategory: RMCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rMCategoryPopupRoute: Routes = [
    {
        path: 'rm-category/:id/delete',
        component: RMCategoryDeletePopupComponent,
        resolve: {
            rMCategory: RMCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
