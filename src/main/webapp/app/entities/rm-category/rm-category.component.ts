import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRMCategory } from 'app/shared/model/rm-category.model';
import { Principal } from 'app/core';
import { RMCategoryService } from './rm-category.service';

@Component({
    selector: '-rm-category',
    templateUrl: './rm-category.component.html'
})
export class RMCategoryComponent implements OnInit, OnDestroy {
    rMCategories: IRMCategory[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private rMCategoryService: RMCategoryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.rMCategoryService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IRMCategory[]>) => (this.rMCategories = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.rMCategoryService.query().subscribe(
            (res: HttpResponse<IRMCategory[]>) => {
                this.rMCategories = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRMCategories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRMCategory) {
        return item.id;
    }

    registerChangeInRMCategories() {
        this.eventSubscriber = this.eventManager.subscribe('rMCategoryListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
