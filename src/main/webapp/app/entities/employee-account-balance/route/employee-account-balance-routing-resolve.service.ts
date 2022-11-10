import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeAccountBalance } from '../employee-account-balance.model';
import { EmployeeAccountBalanceService } from '../service/employee-account-balance.service';

@Injectable({ providedIn: 'root' })
export class EmployeeAccountBalanceRoutingResolveService implements Resolve<IEmployeeAccountBalance | null> {
  constructor(protected service: EmployeeAccountBalanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeAccountBalance | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeAccountBalance: HttpResponse<IEmployeeAccountBalance>) => {
          if (employeeAccountBalance.body) {
            return of(employeeAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
