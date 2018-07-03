/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMCategoryDetailComponent } from 'app/entities/rm-category/rm-category-detail.component';
import { RMCategory } from 'app/shared/model/rm-category.model';

describe('Component Tests', () => {
    describe('RMCategory Management Detail Component', () => {
        let comp: RMCategoryDetailComponent;
        let fixture: ComponentFixture<RMCategoryDetailComponent>;
        const route = ({ data: of({ rMCategory: new RMCategory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMCategoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RMCategoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMCategoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.rMCategory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
