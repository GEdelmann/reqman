import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { RMProject } from 'app/shared/model/rm-project.model';
import { RMProjectService } from './rm-project.service';
import { RMProjectComponent } from './rm-project.component';
import { RMProjectDetailComponent } from './rm-project-detail.component';
import { RMProjectUpdateComponent } from './rm-project-update.component';
import { RMProjectDeletePopupComponent } from './rm-project-delete-dialog.component';
import { IRMProject } from 'app/shared/model/rm-project.model';

@Injectable({ providedIn: 'root' })
export class RMProjectResolve implements Resolve<IRMProject> {
    constructor(private service: RMProjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((rMProject: HttpResponse<RMProject>) => rMProject.body);
        }
        return Observable.of(new RMProject());
    }
}

export const rMProjectRoute: Routes = [
    {
        path: 'rm-project',
        component: RMProjectComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMProject.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-project/:id/view',
        component: RMProjectDetailComponent,
        resolve: {
            rMProject: RMProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMProject.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-project/new',
        component: RMProjectUpdateComponent,
        resolve: {
            rMProject: RMProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMProject.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-project/:id/edit',
        component: RMProjectUpdateComponent,
        resolve: {
            rMProject: RMProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMProject.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rMProjectPopupRoute: Routes = [
    {
        path: 'rm-project/:id/delete',
        component: RMProjectDeletePopupComponent,
        resolve: {
            rMProject: RMProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
