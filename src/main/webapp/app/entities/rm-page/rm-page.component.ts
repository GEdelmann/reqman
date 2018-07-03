import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRMPage } from 'app/shared/model/rm-page.model';
import { Principal } from 'app/core';
import { RMPageService } from './rm-page.service';

@Component({
    selector: '-rm-page',
    templateUrl: './rm-page.component.html'
})
export class RMPageComponent implements OnInit, OnDestroy {
    rMPages: IRMPage[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private rMPageService: RMPageService,
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
            this.rMPageService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IRMPage[]>) => (this.rMPages = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.rMPageService.query().subscribe(
            (res: HttpResponse<IRMPage[]>) => {
                this.rMPages = res.body;
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
        this.registerChangeInRMPages();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRMPage) {
        return item.id;
    }

    registerChangeInRMPages() {
        this.eventSubscriber = this.eventManager.subscribe('rMPageListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
