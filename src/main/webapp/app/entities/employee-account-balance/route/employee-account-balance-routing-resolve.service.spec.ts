import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEmployeeAccountBalance } from '../employee-account-balance.model';
import { EmployeeAccountBalanceService } from '../service/employee-account-balance.service';

import { EmployeeAccountBalanceRoutingResolveService } from './employee-account-balance-routing-resolve.service';

describe('EmployeeAccountBalance routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EmployeeAccountBalanceRoutingResolveService;
  let service: EmployeeAccountBalanceService;
  let resultEmployeeAccountBalance: IEmployeeAccountBalance | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(EmployeeAccountBalanceRoutingResolveService);
    service = TestBed.inject(EmployeeAccountBalanceService);
    resultEmployeeAccountBalance = undefined;
  });

  describe('resolve', () => {
    it('should return IEmployeeAccountBalance returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeAccountBalance = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeAccountBalance).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeAccountBalance = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEmployeeAccountBalance).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IEmployeeAccountBalance>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeAccountBalance = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeAccountBalance).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
