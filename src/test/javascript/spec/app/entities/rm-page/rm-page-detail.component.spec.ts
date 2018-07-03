/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMPageDetailComponent } from 'app/entities/rm-page/rm-page-detail.component';
import { RMPage } from 'app/shared/model/rm-page.model';

describe('Component Tests', () => {
    describe('RMPage Management Detail Component', () => {
        let comp: RMPageDetailComponent;
        let fixture: ComponentFixture<RMPageDetailComponent>;
        const route = ({ data: of({ rMPage: new RMPage(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMPageDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RMPageDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMPageDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.rMPage).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
