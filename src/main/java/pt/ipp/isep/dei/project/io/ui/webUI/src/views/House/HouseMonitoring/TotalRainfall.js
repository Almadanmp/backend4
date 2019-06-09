import React, {Component} from 'react';
import {connect} from 'react-redux';
import {fetchTotalRainfallDay} from './USRedux/US620Redux/Actions620';
import {Button, Card, CardBody, Collapse} from "reactstrap";
import DatePickerOneDay620 from "./USRedux/US620Redux/DatePickerOneDay620.js";

class US620 extends Component {
  constructor(props) {
    super(props);
    this.toggle = this.toggle.bind(this);
    this.state = {
      collapse: false,
      selectedDay: undefined
    };
  }

  componentDidMount() {
    this.props.onFetchTotalRainfall(this.state.selectedDay);
  }

  handleDayPicker = (selectedDay) => {

    console.log("handleDayPicker:" + JSON.stringify(selectedDay))
    if (selectedDay !== undefined) {
      const initialDay = selectedDay.toISOString().substring(0, 10);
      this.setState({selectedDay: selectedDay});
      this.props.onFetchTotalRainfall(initialDay)

    }
  }

  toggle() {
    this.setState(state => ({collapse: !state.collapse}));
  }

  render() {
    const numberOfMonths = 1;
      if (localStorage.getItem("user").includes("admin")) {
        return (
          <>
                <CardBody>
                 ERROR: Non-authorized user.
                </CardBody>
          </>
        )
      } else {
        if ((this.props.totalRainfall.toString()).indexOf("ERROR") != -1) {
          return (
            <>
                <Card>
                  <CardBody>
                    ERROR: No Data Available.
                  </CardBody>
                </Card>
            </>
          )
        } else {
          const {totalRainfall} = this.props;
          return (
            <>
                  <CardBody>
                    <DatePickerOneDay620 getDays={this.handleDayPicker} numberOfMonths={numberOfMonths}/>
                    <h5 key={totalRainfall}>The total rainfall was {totalRainfall} </h5>
                  </CardBody>
            </>
          );
        }
      }
    }
}

const mapStateToProps = (state) => {
  return {
    loading: state.Reducers620.loading,
    totalRainfall: state.Reducers620.totalRainfall,
    error: state.Reducers620.error
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onFetchTotalRainfall: (selectedDay) => {
      dispatch(fetchTotalRainfallDay({selectedDay}))
    }

  }
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(US620);